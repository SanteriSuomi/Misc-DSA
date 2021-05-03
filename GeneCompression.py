from sys import getsizeof


class CompressedGene:
    bit_string = 1

    def __init__(self, gene) -> None:
        self._compress(gene)

    def to_bin(self) -> str:
        return bin(self.bit_string).replace("0b", "")

    def __str__(self) -> str:
        return self._decompress()

    def _compress(self, gene) -> None:
        for n in gene.upper():
            self.bit_string <<= 2
            if n == "A":
                self.bit_string |= 0b00
            elif n == "C":
                self.bit_string |= 0b01
            elif n == "G":
                self.bit_string |= 0b10
            elif n == "T":
                self.bit_string |= 0b11
            else:
                raise ValueError("Invalid Nucleotide:{}".format(n))

    def _decompress(self) -> str:
        gene = ""
        for i in range(0, self.bit_string.bit_length() - 1, 2):
            bits = self.bit_string >> i & 0b11
            if bits == 0b00:
                gene += "A"
            elif bits == 0b01:
                gene += "C"
            elif bits == 0b10:
                gene += "G"
            elif bits == 0b11:
                gene += "T"
            else:
                raise ValueError("Invalid bits:{}".format(bits))
        return gene[::-1]


gene = "ATGCCCAGTTACGAATTTTTAAC" * 1000
o_size = getsizeof(gene)
print("Original size:", o_size)
cg = CompressedGene(gene)
a_size = getsizeof(cg.bit_string)
print("Compressed size:", a_size)
print("Size reduction of", (1 - a_size / o_size) * 100, "percent")
