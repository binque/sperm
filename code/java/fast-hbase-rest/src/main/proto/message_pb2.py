# Generated by the protocol buffer compiler.  DO NOT EDIT!

from google.protobuf import descriptor
from google.protobuf import message
from google.protobuf import reflection
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)


DESCRIPTOR = descriptor.FileDescriptor(
  name='src/main/proto/message.proto',
  package='com.dirlt.java.FastHbaseRest',
  serialized_pb='\n\x1csrc/main/proto/message.proto\x12\x1c\x63om.dirlt.java.FastHbaseRest\"Y\n\x07Request\x12\x12\n\ntable_name\x18\x01 \x02(\t\x12\x0f\n\x07row_key\x18\x02 \x02(\t\x12\x15\n\rcolumn_family\x18\x03 \x02(\t\x12\x12\n\nqualifiers\x18\x05 \x03(\t\"\xb4\x01\n\x08Response\x12\x12\n\ntable_name\x18\x01 \x02(\t\x12\x0f\n\x07row_key\x18\x02 \x02(\t\x12\x15\n\rcolumn_family\x18\x03 \x02(\t\x12<\n\x03kvs\x18\x05 \x03(\x0b\x32/.com.dirlt.java.FastHbaseRest.Response.KeyValue\x1a.\n\x08KeyValue\x12\x11\n\tqualifier\x18\x01 \x02(\t\x12\x0f\n\x07\x63ontent\x18\x03 \x02(\x0c\x42\x10\x42\x0eMessageProtos1')




_REQUEST = descriptor.Descriptor(
  name='Request',
  full_name='com.dirlt.java.FastHbaseRest.Request',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    descriptor.FieldDescriptor(
      name='table_name', full_name='com.dirlt.java.FastHbaseRest.Request.table_name', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='row_key', full_name='com.dirlt.java.FastHbaseRest.Request.row_key', index=1,
      number=2, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='column_family', full_name='com.dirlt.java.FastHbaseRest.Request.column_family', index=2,
      number=3, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='qualifiers', full_name='com.dirlt.java.FastHbaseRest.Request.qualifiers', index=3,
      number=5, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=62,
  serialized_end=151,
)


_RESPONSE_KEYVALUE = descriptor.Descriptor(
  name='KeyValue',
  full_name='com.dirlt.java.FastHbaseRest.Response.KeyValue',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    descriptor.FieldDescriptor(
      name='qualifier', full_name='com.dirlt.java.FastHbaseRest.Response.KeyValue.qualifier', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='content', full_name='com.dirlt.java.FastHbaseRest.Response.KeyValue.content', index=1,
      number=3, type=12, cpp_type=9, label=2,
      has_default_value=False, default_value="",
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=288,
  serialized_end=334,
)

_RESPONSE = descriptor.Descriptor(
  name='Response',
  full_name='com.dirlt.java.FastHbaseRest.Response',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    descriptor.FieldDescriptor(
      name='table_name', full_name='com.dirlt.java.FastHbaseRest.Response.table_name', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='row_key', full_name='com.dirlt.java.FastHbaseRest.Response.row_key', index=1,
      number=2, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='column_family', full_name='com.dirlt.java.FastHbaseRest.Response.column_family', index=2,
      number=3, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='kvs', full_name='com.dirlt.java.FastHbaseRest.Response.kvs', index=3,
      number=5, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[_RESPONSE_KEYVALUE, ],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=154,
  serialized_end=334,
)


_RESPONSE_KEYVALUE.containing_type = _RESPONSE;
_RESPONSE.fields_by_name['kvs'].message_type = _RESPONSE_KEYVALUE

class Request(message.Message):
  __metaclass__ = reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _REQUEST
  
  # @@protoc_insertion_point(class_scope:com.dirlt.java.FastHbaseRest.Request)

class Response(message.Message):
  __metaclass__ = reflection.GeneratedProtocolMessageType
  
  class KeyValue(message.Message):
    __metaclass__ = reflection.GeneratedProtocolMessageType
    DESCRIPTOR = _RESPONSE_KEYVALUE
    
    # @@protoc_insertion_point(class_scope:com.dirlt.java.FastHbaseRest.Response.KeyValue)
  DESCRIPTOR = _RESPONSE
  
  # @@protoc_insertion_point(class_scope:com.dirlt.java.FastHbaseRest.Response)

# @@protoc_insertion_point(module_scope)
