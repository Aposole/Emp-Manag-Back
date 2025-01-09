import os
import base64

key = os.urandom(32)  # Generates 32 random bytes
encoded_key = base64.b64encode(key).decode('utf-8')
print(encoded_key)