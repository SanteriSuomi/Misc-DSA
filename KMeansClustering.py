from statistics import mean, pstdev
from math import sqrt
from random import uniform
from functools import partial
from copy import deepcopy
from random import randint
from re import split
import csv


def z_scores(original_sequence):
    avg = mean(original_sequence)
    std = pstdev(original_sequence)
    if std == 0:
        return [0]
    return [(x - avg) / std for x in original_sequence]


class DataPoint:
    def __init__(self, initial):
        self._originals = tuple(initial)
        self.dimensions = tuple(initial)

    def num_dimensions(self):
        return len(self.dimensions)

    def distance(self, other):
        combined = zip(self.dimensions, other.dimensions)
        differences = [(x - y) ** 2 for x, y in combined]
        return sqrt(sum(differences))

    def __eq__(self, other):
        if not isinstance(other, DataPoint):
            return NotImplemented
        return self.dimensions == other.dimensions

    def __repr__(self):
        return self._originals.__repr__()


class Governor(DataPoint):
    # Represent US governors by longitude and age as a data point, where they server as dimensions
    def __init__(self, longitude, age, state):
        super().__init__([longitude, age])
        self.longitude = longitude
        self.age = age
        self.state = state

    def __repr__(self):
        return f"{self.state}: (longitude: {self.longitude}, age: {self.age})\n"


class School(DataPoint):
    def __init__(self, city, school, num_students, distance_from_helsinki):
        super().__init__([num_students, distance_from_helsinki])
        self.city = city
        self.school = school
        self.num_students = num_students
        self.distance_from_helsinki = distance_from_helsinki

    def __repr__(self):
        return f"{self.city}, {self.school}: Students: {self.num_students}, Distance: {self.distance_from_helsinki}\n"


class KMeans:
    class Cluster:
        def __init__(self, points, centroid):
            self.points = points
            self.centroid = centroid

    def __init__(self, k, points):
        if k < 1:
            raise ValueError("k may not be less than 1")
        self._points = points  # All points in the dataset
        self._zscore_normalize()
        self._clusters = []
        for _ in range(k):
            random_point = self._random_point()
            cluster = KMeans.Cluster([], random_point)
            self._clusters.append(cluster)

    def run(self, max_iterations=100):
        for it in range(max_iterations):
            for cluster in self._clusters:
                cluster.points.clear()
            self._assign_clusters()
            old_centroids = deepcopy(self._get_centroids())
            self._generate_centroids()
            if old_centroids == self._get_centroids():
                print(f"Converged after {it} iterations.")
                return self._clusters
        return self._clusters

    def _random_point(self):
        # Used to create random initial centroids for each of the clusters
        random_dimensions = []
        for d in range(self._points[0].num_dimensions()):
            values = self._dimension_slice(d)
            random_value = uniform(min(values), max(values))
            random_dimensions.append(random_value)
        return DataPoint(random_dimensions)

    def _get_centroids(self):
        # Get all centroids associated with the clusters
        return [x.centroid for x in self._clusters]

    def _dimension_slice(self, dimension):
        # Return a list of the value of given dimension of every data point
        return [x.dimensions[dimension] for x in self._points]

    def _zscore_normalize(self):
        zscored = [[] for _ in range(len(self._points))]
        for d in range(self._points[0].num_dimensions()):  # For each dimension
            d_slice = self._dimension_slice(d)
            for i, zscore in enumerate(z_scores(d_slice)):
                zscored[i].append(zscore)
        for i in range(len(self._points)):
            self._points[i].dimensions = tuple(zscored[i])

    def _assign_clusters(self):
        for point in self._points:
            closest = min(self._get_centroids(), key=partial(DataPoint.distance, point))
            ind = self._get_centroids().index(closest)
            cluster = self._clusters[ind]
            cluster.points.append(point)

    def _generate_centroids(self):
        for cluster in self._clusters:
            if len(cluster.points) == 0:
                continue
            means = []
            for d in range(cluster.points[0].num_dimensions()):
                dimension_slice = [p.dimensions[d] for p in cluster.points]
                means.append(mean(dimension_slice))
            cluster.centroid = DataPoint(means)


# random_data_points = []
# for i in range(5):
#     random_data_points.append(
#         DataPoint([randint(0, 100), randint(0, 100), randint(0, 100)])
#     )
# kmeans_test = KMeans(
#     2,  # To how many clusters data points will be clustered to
#     random_data_points,
# )
# test_clusters = kmeans_test.run()
# for ind, cluster in enumerate(test_clusters):
#     print(f"Cluster {ind}: {cluster.points}")

# governor_data_points = [
#     Governor(-86.79113, 72, "Alabama"),
#     Governor(-152.404419, 66, "Alaska"),
#     Governor(-111.431221, 53, "Arizona"),
#     Governor(-92.373123, 66, "Arkansas"),
#     Governor(-119.681564, 79, "California"),
#     Governor(-105.311104, 65, "Colorado"),
#     Governor(-72.755371, 61, "Connecticut"),
#     Governor(-75.507141, 61, "Delaware"),
#     Governor(-81.686783, 64, "Florida"),
#     Governor(-83.643074, 74, "Georgia"),
#     Governor(-157.498337, 60, "Hawaii"),
#     Governor(-114.478828, 75, "Idaho"),
#     Governor(-88.986137, 60, "Illinois"),
#     Governor(-86.258278, 49, "Indiana"),
#     Governor(-93.210526, 57, "Iowa"),
#     Governor(-96.726486, 60, "Kansas"),
#     Governor(-84.670067, 50, "Kentucky"),
#     Governor(-91.867805, 50, "Louisiana"),
#     Governor(-69.381927, 68, "Maine"),
#     Governor(-76.802101, 61, "Maryland"),
#     Governor(-71.530106, 60, "Massachusetts"),
#     Governor(-84.536095, 58, "Michigan"),
#     Governor(-93.900192, 70, "Minnesota"),
#     Governor(-89.678696, 62, "Mississippi"),
#     Governor(-92.288368, 43, "Missouri"),
#     Governor(-110.454353, 51, "Montana"),
#     Governor(-98.268082, 52, "Nebraska"),
#     Governor(-117.055374, 53, "Nevada"),
#     Governor(-71.563896, 42, "New Hampshire"),
#     Governor(-74.521011, 54, "New Jersey"),
#     Governor(-106.248482, 57, "New Mexico"),
#     Governor(-74.948051, 59, "New York"),
#     Governor(-79.806419, 60, "North Carolina"),
#     Governor(-99.784012, 60, "North Dakota"),
#     Governor(-82.764915, 65, "Ohio"),
#     Governor(-96.928917, 62, "Oklahoma"),
#     Governor(-122.070938, 56, "Oregon"),
#     Governor(-77.209755, 68, "Pennsylvania"),
#     Governor(-71.51178, 46, "Rhode Island"),
#     Governor(-80.945007, 70, "South Carolina"),
#     Governor(-99.438828, 64, "South Dakota"),
#     Governor(-86.692345, 58, "Tennessee"),
#     Governor(-97.563461, 59, "Texas"),
#     Governor(-111.862434, 70, "Utah"),
#     Governor(-72.710686, 58, "Vermont"),
#     Governor(-78.169968, 60, "Virginia"),
#     Governor(-121.490494, 66, "Washington"),
#     Governor(-80.954453, 66, "West Virginia"),
#     Governor(-89.616508, 49, "Wisconsin"),
#     Governor(-107.30249, 55, "Wyoming"),
# ]
# kmeans = KMeans(2, governor_data_points)
# clusters = kmeans.run()
# for ind, cluster in enumerate(clusters):
#     print(f"Cluster: {ind}:\n {cluster.points}")


def read_data(file):
    # Quick function to read data from a CSV file
    data = []
    with open(file, "r") as file:
        reader = csv.reader(file)
        next(reader)
        for row in reader:
            fields_str = row[0]
            row_fields = split("\t", fields_str)
            rows = []
            for s in row_fields:
                rows.append(s.replace(" ", ""))
            data.append(School(rows[0], rows[1], int(rows[2]), int(rows[3])))
    return data


# Read data from from a CSV file that contains Finland's UAS schools, and cluster them based on student count and distance from the capital, Helsinki.
# Based on my personal findings on the clustering, it seems that the further you go from the capital, the less students schools have, on average (which obviously makes sense).
school_data_points = read_data("schools_data_points.csv")
k_means = KMeans(2, school_data_points)
clusters = k_means.run()
for index, cluster in enumerate(clusters):
    print(f"Cluster: {index}:\n {cluster.points}")
