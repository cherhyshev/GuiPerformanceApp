package ru.hse.spb.common.protocol;// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: array_message.proto

public final class Messages {
  private Messages() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface ArrayMessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:ArrayMessage)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int32 elementsNum = 1;</code>
     */
    int getElementsNum();

    /**
     * <code>repeated int32 arrayElements = 2;</code>
     */
    java.util.List<java.lang.Integer> getArrayElementsList();
    /**
     * <code>repeated int32 arrayElements = 2;</code>
     */
    int getArrayElementsCount();
    /**
     * <code>repeated int32 arrayElements = 2;</code>
     */
    int getArrayElements(int index);
  }
  /**
   * Protobuf type {@code ArrayMessage}
   */
  public  static final class ArrayMessage extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:ArrayMessage)
      ArrayMessageOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use ArrayMessage.newBuilder() to construct.
    private ArrayMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private ArrayMessage() {
      elementsNum_ = 0;
      arrayElements_ = java.util.Collections.emptyList();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private ArrayMessage(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {

              elementsNum_ = input.readInt32();
              break;
            }
            case 16: {
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                arrayElements_ = new java.util.ArrayList<java.lang.Integer>();
                mutable_bitField0_ |= 0x00000002;
              }
              arrayElements_.add(input.readInt32());
              break;
            }
            case 18: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002) && input.getBytesUntilLimit() > 0) {
                arrayElements_ = new java.util.ArrayList<java.lang.Integer>();
                mutable_bitField0_ |= 0x00000002;
              }
              while (input.getBytesUntilLimit() > 0) {
                arrayElements_.add(input.readInt32());
              }
              input.popLimit(limit);
              break;
            }
            default: {
              if (!parseUnknownFieldProto3(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
          arrayElements_ = java.util.Collections.unmodifiableList(arrayElements_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return Messages.internal_static_ArrayMessage_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return Messages.internal_static_ArrayMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              Messages.ArrayMessage.class, Messages.ArrayMessage.Builder.class);
    }

    private int bitField0_;
    public static final int ELEMENTSNUM_FIELD_NUMBER = 1;
    private int elementsNum_;
    /**
     * <code>int32 elementsNum = 1;</code>
     */
    public int getElementsNum() {
      return elementsNum_;
    }

    public static final int ARRAYELEMENTS_FIELD_NUMBER = 2;
    private java.util.List<java.lang.Integer> arrayElements_;
    /**
     * <code>repeated int32 arrayElements = 2;</code>
     */
    public java.util.List<java.lang.Integer>
        getArrayElementsList() {
      return arrayElements_;
    }
    /**
     * <code>repeated int32 arrayElements = 2;</code>
     */
    public int getArrayElementsCount() {
      return arrayElements_.size();
    }
    /**
     * <code>repeated int32 arrayElements = 2;</code>
     */
    public int getArrayElements(int index) {
      return arrayElements_.get(index);
    }
    private int arrayElementsMemoizedSerializedSize = -1;

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (elementsNum_ != 0) {
        output.writeInt32(1, elementsNum_);
      }
      if (getArrayElementsList().size() > 0) {
        output.writeUInt32NoTag(18);
        output.writeUInt32NoTag(arrayElementsMemoizedSerializedSize);
      }
      for (int i = 0; i < arrayElements_.size(); i++) {
        output.writeInt32NoTag(arrayElements_.get(i));
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (elementsNum_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, elementsNum_);
      }
      {
        int dataSize = 0;
        for (int i = 0; i < arrayElements_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeInt32SizeNoTag(arrayElements_.get(i));
        }
        size += dataSize;
        if (!getArrayElementsList().isEmpty()) {
          size += 1;
          size += com.google.protobuf.CodedOutputStream
              .computeInt32SizeNoTag(dataSize);
        }
        arrayElementsMemoizedSerializedSize = dataSize;
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof Messages.ArrayMessage)) {
        return super.equals(obj);
      }
      Messages.ArrayMessage other = (Messages.ArrayMessage) obj;

      boolean result = true;
      result = result && (getElementsNum()
          == other.getElementsNum());
      result = result && getArrayElementsList()
          .equals(other.getArrayElementsList());
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + ELEMENTSNUM_FIELD_NUMBER;
      hash = (53 * hash) + getElementsNum();
      if (getArrayElementsCount() > 0) {
        hash = (37 * hash) + ARRAYELEMENTS_FIELD_NUMBER;
        hash = (53 * hash) + getArrayElementsList().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static Messages.ArrayMessage parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Messages.ArrayMessage parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Messages.ArrayMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Messages.ArrayMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Messages.ArrayMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Messages.ArrayMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Messages.ArrayMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static Messages.ArrayMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static Messages.ArrayMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static Messages.ArrayMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static Messages.ArrayMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static Messages.ArrayMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(Messages.ArrayMessage prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code ArrayMessage}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:ArrayMessage)
        Messages.ArrayMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return Messages.internal_static_ArrayMessage_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return Messages.internal_static_ArrayMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                Messages.ArrayMessage.class, Messages.ArrayMessage.Builder.class);
      }

      // Construct using Messages.ArrayMessage.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        elementsNum_ = 0;

        arrayElements_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return Messages.internal_static_ArrayMessage_descriptor;
      }

      @java.lang.Override
      public Messages.ArrayMessage getDefaultInstanceForType() {
        return Messages.ArrayMessage.getDefaultInstance();
      }

      @java.lang.Override
      public Messages.ArrayMessage build() {
        Messages.ArrayMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public Messages.ArrayMessage buildPartial() {
        Messages.ArrayMessage result = new Messages.ArrayMessage(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        result.elementsNum_ = elementsNum_;
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          arrayElements_ = java.util.Collections.unmodifiableList(arrayElements_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.arrayElements_ = arrayElements_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return (Builder) super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof Messages.ArrayMessage) {
          return mergeFrom((Messages.ArrayMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(Messages.ArrayMessage other) {
        if (other == Messages.ArrayMessage.getDefaultInstance()) return this;
        if (other.getElementsNum() != 0) {
          setElementsNum(other.getElementsNum());
        }
        if (!other.arrayElements_.isEmpty()) {
          if (arrayElements_.isEmpty()) {
            arrayElements_ = other.arrayElements_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureArrayElementsIsMutable();
            arrayElements_.addAll(other.arrayElements_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        Messages.ArrayMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (Messages.ArrayMessage) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int elementsNum_ ;
      /**
       * <code>int32 elementsNum = 1;</code>
       */
      public int getElementsNum() {
        return elementsNum_;
      }
      /**
       * <code>int32 elementsNum = 1;</code>
       */
      public Builder setElementsNum(int value) {
        
        elementsNum_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 elementsNum = 1;</code>
       */
      public Builder clearElementsNum() {
        
        elementsNum_ = 0;
        onChanged();
        return this;
      }

      private java.util.List<java.lang.Integer> arrayElements_ = java.util.Collections.emptyList();
      private void ensureArrayElementsIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          arrayElements_ = new java.util.ArrayList<java.lang.Integer>(arrayElements_);
          bitField0_ |= 0x00000002;
         }
      }
      /**
       * <code>repeated int32 arrayElements = 2;</code>
       */
      public java.util.List<java.lang.Integer>
          getArrayElementsList() {
        return java.util.Collections.unmodifiableList(arrayElements_);
      }
      /**
       * <code>repeated int32 arrayElements = 2;</code>
       */
      public int getArrayElementsCount() {
        return arrayElements_.size();
      }
      /**
       * <code>repeated int32 arrayElements = 2;</code>
       */
      public int getArrayElements(int index) {
        return arrayElements_.get(index);
      }
      /**
       * <code>repeated int32 arrayElements = 2;</code>
       */
      public Builder setArrayElements(
          int index, int value) {
        ensureArrayElementsIsMutable();
        arrayElements_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 arrayElements = 2;</code>
       */
      public Builder addArrayElements(int value) {
        ensureArrayElementsIsMutable();
        arrayElements_.add(value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 arrayElements = 2;</code>
       */
      public Builder addAllArrayElements(
          java.lang.Iterable<? extends java.lang.Integer> values) {
        ensureArrayElementsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, arrayElements_);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 arrayElements = 2;</code>
       */
      public Builder clearArrayElements() {
        arrayElements_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFieldsProto3(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:ArrayMessage)
    }

    // @@protoc_insertion_point(class_scope:ArrayMessage)
    private static final Messages.ArrayMessage DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new Messages.ArrayMessage();
    }

    public static Messages.ArrayMessage getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<ArrayMessage>
        PARSER = new com.google.protobuf.AbstractParser<ArrayMessage>() {
      @java.lang.Override
      public ArrayMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ArrayMessage(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<ArrayMessage> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<ArrayMessage> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public Messages.ArrayMessage getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ArrayMessage_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ArrayMessage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\023array_message.proto\":\n\014ArrayMessage\022\023\n" +
      "\013elementsNum\030\001 \001(\005\022\025\n\rarrayElements\030\002 \003(" +
      "\005B\nB\010Messagesb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_ArrayMessage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_ArrayMessage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ArrayMessage_descriptor,
        new java.lang.String[] { "ElementsNum", "ArrayElements", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
