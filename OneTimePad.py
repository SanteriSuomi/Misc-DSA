from secrets import token_bytes
from PIL import Image as img


class one_time_pad:
    _dummy = 0
    _encrypted = 0

    def encrypt(self, data):
        original = int.from_bytes(data.encode(), "big")
        self._dummy = self._get_dummy_keys(len(data))
        self._encrypted = original ^ self._dummy

    def decrypt(self):
        decrypted = self._dummy ^ self._encrypted
        temp = decrypted.to_bytes(decrypted.bit_length(), "big")
        return temp.decode()

    def _get_dummy_keys(self, length):
        random_byte_string = token_bytes(length)
        return int.from_bytes(random_byte_string, "big")


# encrypt string
data = "HURRDURR"
pad = one_time_pad()
pad.encrypt(data)
print(pad.decrypt())
