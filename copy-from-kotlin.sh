#!/usr/bin/env bash

set -e -x

KOTLIN=../kotlin

OJK=org/jetbrains/kotlin
S=$KOTLIN/core/deserialization/src/$OJK
J=$KOTLIN/core/descriptors.jvm/src/$OJK
CUR=src/main/java/$OJK

mkdir -p $CUR/{serialization/deserialization,serialization/jvm,load/kotlin}

cp $KOTLIN/core/deserialization/src/descriptors.proto src/
cp $KOTLIN/core/descriptors.jvm/src/jvm_descriptors.proto src/

cp $S/serialization/{Flags.java,ProtoBuf.java,ProtoDatas.kt} $CUR/serialization/
cp $S/serialization/deserialization/{NameResolver.java,TypeTable.kt,protoTypeTableUtil.kt} $CUR/serialization/deserialization/
cp $J/load/kotlin/JvmNameResolver.kt $CUR/load/kotlin/
cp $J/serialization/jvm/{BitEncoding.java,utfEncoding.kt,JvmProtoBuf.java,JvmProtoBufUtil.kt} $CUR/serialization/jvm/

for file in $(find $CUR -type f);
do
    sed -i.bak -e 's/org.jetbrains.kotlin.protobuf/com.google.protobuf/g' $file
    rm $file.bak
done
