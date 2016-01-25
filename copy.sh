#!/bin/bash

KOTLIN=../kotlin

OJK=org/jetbrains/kotlin
S=$KOTLIN/core/deserialization/src/$OJK
J=$KOTLIN/core/descriptor.loader.java/src/$OJK
CUR=src/main/java/$OJK

mkdir -p $CUR/{serialization/deserialization,serialization/jvm,load/kotlin}

cp $S/serialization/{ProtoBuf.java,ProtoDatas.kt} $CUR/serialization/
cp $S/serialization/deserialization/{NameResolver.java,TypeTable.kt,protoTypeTableUtil.kt} $CUR/serialization/deserialization/
cp $J/load/kotlin/JvmNameResolver.kt $CUR/load/kotlin/
cp $J/serialization/jvm/{BitEncoding.java,utfEncoding.kt,JvmProtoBuf.java,JvmProtoBufUtil.kt} $CUR/serialization/jvm/
