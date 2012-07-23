// Generated by the protocol buffer compiler.  DO NOT EDIT!

#define INTERNAL_SUPPRESS_PROTOBUF_FIELD_DEPRECATION
#include "echo.pb.h"

#include <algorithm>

#include <google/protobuf/stubs/once.h>
#include <google/protobuf/io/coded_stream.h>
#include <google/protobuf/wire_format_lite_inl.h>
#include <google/protobuf/descriptor.h>
#include <google/protobuf/reflection_ops.h>
#include <google/protobuf/wire_format.h>
// @@protoc_insertion_point(includes)

namespace sample {

namespace {

const ::google::protobuf::Descriptor* Echo_descriptor_ = NULL;
const ::google::protobuf::internal::GeneratedMessageReflection*
Echo_reflection_ = NULL;

}  // namespace


void protobuf_AssignDesc_echo_2eproto() {
  protobuf_AddDesc_echo_2eproto();
  const ::google::protobuf::FileDescriptor* file =
    ::google::protobuf::DescriptorPool::generated_pool()->FindFileByName(
      "echo.proto");
  GOOGLE_CHECK(file != NULL);
  Echo_descriptor_ = file->message_type(0);
  static const int Echo_offsets_[1] = {
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(Echo, content_),
  };
  Echo_reflection_ =
    new ::google::protobuf::internal::GeneratedMessageReflection(
    Echo_descriptor_,
    Echo::default_instance_,
    Echo_offsets_,
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(Echo, _has_bits_[0]),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(Echo, _unknown_fields_),
    -1,
    ::google::protobuf::DescriptorPool::generated_pool(),
    ::google::protobuf::MessageFactory::generated_factory(),
    sizeof(Echo));
}

namespace {

GOOGLE_PROTOBUF_DECLARE_ONCE(protobuf_AssignDescriptors_once_);
inline void protobuf_AssignDescriptorsOnce() {
  ::google::protobuf::GoogleOnceInit(&protobuf_AssignDescriptors_once_,
                                     &protobuf_AssignDesc_echo_2eproto);
}

void protobuf_RegisterTypes(const ::std::string&) {
  protobuf_AssignDescriptorsOnce();
  ::google::protobuf::MessageFactory::InternalRegisterGeneratedMessage(
    Echo_descriptor_, &Echo::default_instance());
}

}  // namespace

void protobuf_ShutdownFile_echo_2eproto() {
  delete Echo::default_instance_;
  delete Echo_reflection_;
}

void protobuf_AddDesc_echo_2eproto() {
  static bool already_here = false;
  if (already_here) return;
  already_here = true;
  GOOGLE_PROTOBUF_VERIFY_VERSION;

  ::google::protobuf::DescriptorPool::InternalAddGeneratedFile(
    "\n\necho.proto\022\006sample\"\027\n\004Echo\022\017\n\007content\030"
    "\001 \001(\t", 45);
  ::google::protobuf::MessageFactory::InternalRegisterGeneratedFile(
    "echo.proto", &protobuf_RegisterTypes);
  Echo::default_instance_ = new Echo();
  Echo::default_instance_->InitAsDefaultInstance();
  ::google::protobuf::internal::OnShutdown(&protobuf_ShutdownFile_echo_2eproto);
}

// Force AddDescriptors() to be called at static initialization time.
struct StaticDescriptorInitializer_echo_2eproto {
  StaticDescriptorInitializer_echo_2eproto() {
    protobuf_AddDesc_echo_2eproto();
  }
} static_descriptor_initializer_echo_2eproto_;


// ===================================================================

#ifndef _MSC_VER
const int Echo::kContentFieldNumber;
#endif  // !_MSC_VER

Echo::Echo()
  : ::google::protobuf::Message() {
  SharedCtor();
}

void Echo::InitAsDefaultInstance() {
}

Echo::Echo(const Echo& from)
  : ::google::protobuf::Message() {
  SharedCtor();
  MergeFrom(from);
}

void Echo::SharedCtor() {
  _cached_size_ = 0;
  content_ = const_cast< ::std::string*>(&::google::protobuf::internal::kEmptyString);
  ::memset(_has_bits_, 0, sizeof(_has_bits_));
}

Echo::~Echo() {
  SharedDtor();
}

void Echo::SharedDtor() {
  if (content_ != &::google::protobuf::internal::kEmptyString) {
    delete content_;
  }
  if (this != default_instance_) {
  }
}

void Echo::SetCachedSize(int size) const {
  GOOGLE_SAFE_CONCURRENT_WRITES_BEGIN();
  _cached_size_ = size;
  GOOGLE_SAFE_CONCURRENT_WRITES_END();
}
const ::google::protobuf::Descriptor* Echo::descriptor() {
  protobuf_AssignDescriptorsOnce();
  return Echo_descriptor_;
}

const Echo& Echo::default_instance() {
  if (default_instance_ == NULL) protobuf_AddDesc_echo_2eproto();
  return *default_instance_;
}

Echo* Echo::default_instance_ = NULL;

Echo* Echo::New() const {
  return new Echo;
}

void Echo::Clear() {
  if (_has_bits_[0 / 32] & (0xffu << (0 % 32))) {
    if (has_content()) {
      if (content_ != &::google::protobuf::internal::kEmptyString) {
        content_->clear();
      }
    }
  }
  ::memset(_has_bits_, 0, sizeof(_has_bits_));
  mutable_unknown_fields()->Clear();
}

bool Echo::MergePartialFromCodedStream(
  ::google::protobuf::io::CodedInputStream* input) {
#define DO_(EXPRESSION) if (!(EXPRESSION)) return false
  ::google::protobuf::uint32 tag;
  while ((tag = input->ReadTag()) != 0) {
    switch (::google::protobuf::internal::WireFormatLite::GetTagFieldNumber(tag)) {
        // optional string content = 1;
      case 1: {
        if (::google::protobuf::internal::WireFormatLite::GetTagWireType(tag) ==
            ::google::protobuf::internal::WireFormatLite::WIRETYPE_LENGTH_DELIMITED) {
          DO_(::google::protobuf::internal::WireFormatLite::ReadString(
                input, this->mutable_content()));
          ::google::protobuf::internal::WireFormat::VerifyUTF8String(
            this->content().data(), this->content().length(),
            ::google::protobuf::internal::WireFormat::PARSE);
        } else {
          goto handle_uninterpreted;
        }
        if (input->ExpectAtEnd()) return true;
        break;
      }

      default: {
      handle_uninterpreted:
        if (::google::protobuf::internal::WireFormatLite::GetTagWireType(tag) ==
            ::google::protobuf::internal::WireFormatLite::WIRETYPE_END_GROUP) {
          return true;
        }
        DO_(::google::protobuf::internal::WireFormat::SkipField(
              input, tag, mutable_unknown_fields()));
        break;
      }
    }
  }
  return true;
#undef DO_
}

void Echo::SerializeWithCachedSizes(
  ::google::protobuf::io::CodedOutputStream* output) const {
  // optional string content = 1;
  if (has_content()) {
    ::google::protobuf::internal::WireFormat::VerifyUTF8String(
      this->content().data(), this->content().length(),
      ::google::protobuf::internal::WireFormat::SERIALIZE);
    ::google::protobuf::internal::WireFormatLite::WriteString(
      1, this->content(), output);
  }

  if (!unknown_fields().empty()) {
    ::google::protobuf::internal::WireFormat::SerializeUnknownFields(
      unknown_fields(), output);
  }
}

::google::protobuf::uint8* Echo::SerializeWithCachedSizesToArray(
  ::google::protobuf::uint8* target) const {
  // optional string content = 1;
  if (has_content()) {
    ::google::protobuf::internal::WireFormat::VerifyUTF8String(
      this->content().data(), this->content().length(),
      ::google::protobuf::internal::WireFormat::SERIALIZE);
    target =
      ::google::protobuf::internal::WireFormatLite::WriteStringToArray(
        1, this->content(), target);
  }

  if (!unknown_fields().empty()) {
    target = ::google::protobuf::internal::WireFormat::SerializeUnknownFieldsToArray(
               unknown_fields(), target);
  }
  return target;
}

int Echo::ByteSize() const {
  int total_size = 0;

  if (_has_bits_[0 / 32] & (0xffu << (0 % 32))) {
    // optional string content = 1;
    if (has_content()) {
      total_size += 1 +
                    ::google::protobuf::internal::WireFormatLite::StringSize(
                      this->content());
    }

  }
  if (!unknown_fields().empty()) {
    total_size +=
      ::google::protobuf::internal::WireFormat::ComputeUnknownFieldsSize(
        unknown_fields());
  }
  GOOGLE_SAFE_CONCURRENT_WRITES_BEGIN();
  _cached_size_ = total_size;
  GOOGLE_SAFE_CONCURRENT_WRITES_END();
  return total_size;
}

void Echo::MergeFrom(const ::google::protobuf::Message& from) {
  GOOGLE_CHECK_NE(&from, this);
  const Echo* source =
    ::google::protobuf::internal::dynamic_cast_if_available<const Echo*>(
      &from);
  if (source == NULL) {
    ::google::protobuf::internal::ReflectionOps::Merge(from, this);
  } else {
    MergeFrom(*source);
  }
}

void Echo::MergeFrom(const Echo& from) {
  GOOGLE_CHECK_NE(&from, this);
  if (from._has_bits_[0 / 32] & (0xffu << (0 % 32))) {
    if (from.has_content()) {
      set_content(from.content());
    }
  }
  mutable_unknown_fields()->MergeFrom(from.unknown_fields());
}

void Echo::CopyFrom(const ::google::protobuf::Message& from) {
  if (&from == this) return;
  Clear();
  MergeFrom(from);
}

void Echo::CopyFrom(const Echo& from) {
  if (&from == this) return;
  Clear();
  MergeFrom(from);
}

bool Echo::IsInitialized() const {

  return true;
}

void Echo::Swap(Echo* other) {
  if (other != this) {
    std::swap(content_, other->content_);
    std::swap(_has_bits_[0], other->_has_bits_[0]);
    _unknown_fields_.Swap(&other->_unknown_fields_);
    std::swap(_cached_size_, other->_cached_size_);
  }
}

::google::protobuf::Metadata Echo::GetMetadata() const {
  protobuf_AssignDescriptorsOnce();
  ::google::protobuf::Metadata metadata;
  metadata.descriptor = Echo_descriptor_;
  metadata.reflection = Echo_reflection_;
  return metadata;
}


// @@protoc_insertion_point(namespace_scope)

}  // namespace sample

// @@protoc_insertion_point(global_scope)
