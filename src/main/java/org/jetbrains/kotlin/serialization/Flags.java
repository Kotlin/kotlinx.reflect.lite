/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.serialization;

import com.google.protobuf.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Flags {
    private Flags() {}

    // Types
    public static final BooleanFlagField SUSPEND_TYPE = FlagField.booleanFirst();

    // Common for declarations

    public static final BooleanFlagField HAS_ANNOTATIONS = FlagField.booleanFirst();
    public static final FlagField<ProtoBuf.Visibility> VISIBILITY = FlagField.after(HAS_ANNOTATIONS, ProtoBuf.Visibility.values());
    public static final FlagField<ProtoBuf.Modality> MODALITY = FlagField.after(VISIBILITY, ProtoBuf.Modality.values());

    // Class

    public static final FlagField<ProtoBuf.Class.Kind> CLASS_KIND = FlagField.after(MODALITY, ProtoBuf.Class.Kind.values());
    public static final BooleanFlagField IS_INNER = FlagField.booleanAfter(CLASS_KIND);
    public static final BooleanFlagField IS_DATA = FlagField.booleanAfter(IS_INNER);
    public static final BooleanFlagField IS_EXTERNAL_CLASS = FlagField.booleanAfter(IS_DATA);
    public static final BooleanFlagField IS_EXPECT_CLASS = FlagField.booleanAfter(IS_EXTERNAL_CLASS);

    // Constructors

    public static final BooleanFlagField IS_SECONDARY = FlagField.booleanAfter(VISIBILITY);

    // Callables

    public static final FlagField<ProtoBuf.MemberKind> MEMBER_KIND = FlagField.after(MODALITY, ProtoBuf.MemberKind.values());

    // Functions

    public static final BooleanFlagField IS_OPERATOR = FlagField.booleanAfter(MEMBER_KIND);
    public static final BooleanFlagField IS_INFIX = FlagField.booleanAfter(IS_OPERATOR);
    public static final BooleanFlagField IS_INLINE = FlagField.booleanAfter(IS_INFIX);
    public static final BooleanFlagField IS_TAILREC = FlagField.booleanAfter(IS_INLINE);
    public static final BooleanFlagField IS_EXTERNAL_FUNCTION = FlagField.booleanAfter(IS_TAILREC);
    public static final BooleanFlagField IS_SUSPEND = FlagField.booleanAfter(IS_EXTERNAL_FUNCTION);
    public static final BooleanFlagField IS_EXPECT_FUNCTION = FlagField.booleanAfter(IS_SUSPEND);

    // Properties

    public static final BooleanFlagField IS_VAR = FlagField.booleanAfter(MEMBER_KIND);
    public static final BooleanFlagField HAS_GETTER = FlagField.booleanAfter(IS_VAR);
    public static final BooleanFlagField HAS_SETTER = FlagField.booleanAfter(HAS_GETTER);
    public static final BooleanFlagField IS_CONST = FlagField.booleanAfter(HAS_SETTER);
    public static final BooleanFlagField IS_LATEINIT = FlagField.booleanAfter(IS_CONST);
    public static final BooleanFlagField HAS_CONSTANT = FlagField.booleanAfter(IS_LATEINIT);
    public static final BooleanFlagField IS_EXTERNAL_PROPERTY = FlagField.booleanAfter(HAS_CONSTANT);
    public static final BooleanFlagField IS_DELEGATED = FlagField.booleanAfter(IS_EXTERNAL_PROPERTY);
    public static final BooleanFlagField IS_EXPECT_PROPERTY = FlagField.booleanAfter(IS_DELEGATED);

    // Parameters

    public static final BooleanFlagField DECLARES_DEFAULT_VALUE = FlagField.booleanAfter(HAS_ANNOTATIONS);
    public static final BooleanFlagField IS_CROSSINLINE = FlagField.booleanAfter(DECLARES_DEFAULT_VALUE);
    public static final BooleanFlagField IS_NOINLINE = FlagField.booleanAfter(IS_CROSSINLINE);

    // Accessors

    public static final BooleanFlagField IS_NOT_DEFAULT = FlagField.booleanAfter(MODALITY);
    public static final BooleanFlagField IS_EXTERNAL_ACCESSOR = FlagField.booleanAfter(IS_NOT_DEFAULT);
    public static final BooleanFlagField IS_INLINE_ACCESSOR = FlagField.booleanAfter(IS_EXTERNAL_ACCESSOR);

    // Contracts expressions
    public static final BooleanFlagField IS_NEGATED = FlagField.booleanFirst();
    public static final BooleanFlagField IS_NULL_CHECK_PREDICATE = FlagField.booleanAfter(IS_NEGATED);


    // ---

    // Infrastructure

    public static abstract class FlagField<E> {
        public static <E extends Internal.EnumLite> FlagField<E> after(FlagField<?> previousField, E[] values) {
            int offset = previousField.offset + previousField.bitWidth;
            return new EnumLiteFlagField<E>(offset, values);
        }

        public static <E extends Internal.EnumLite> FlagField<E> first(E[] values) {
            return new EnumLiteFlagField<E>(0, values);
        }

        public static BooleanFlagField booleanFirst() {
            return new BooleanFlagField(0);
        }

        public static BooleanFlagField booleanAfter(FlagField<?> previousField) {
            int offset = previousField.offset + previousField.bitWidth;
            return new BooleanFlagField(offset);
        }

        protected final int offset;
        protected final int bitWidth;

        private FlagField(int offset, int bitWidth) {
            this.offset = offset;
            this.bitWidth = bitWidth;
        }

        public abstract E get(int flags);

        public abstract int toFlags(E value);
    }

    @SuppressWarnings("WeakerAccess")
    public static class BooleanFlagField extends FlagField<Boolean> {
        public BooleanFlagField(int offset) {
            super(offset, 1);
        }

        @Override
        @NotNull
        public Boolean get(int flags) {
            return (flags & (1 << offset)) != 0;
        }

        @Override
        public int toFlags(Boolean value) {
            return value ? 1 << offset : 0;
        }

        public int invert(int flags) { return (flags ^ (1 << offset)); }
    }

    private static class EnumLiteFlagField<E extends Internal.EnumLite> extends FlagField<E> {
        private final E[] values;

        public EnumLiteFlagField(int offset, E[] values) {
            super(offset, bitWidth(values));
            this.values = values;
        }

        private static <E> int bitWidth(@NotNull E[] enumEntries) {
            int length = enumEntries.length - 1;
            if (length == 0) return 1;
            for (int i = 31; i >= 0; i--) {
                if ((length & (1 << i)) != 0) return i + 1;
            }
            throw new IllegalStateException("Empty enum: " + enumEntries.getClass());
        }

        @Override
        @Nullable
        public E get(int flags) {
            int maskUnshifted = (1 << bitWidth) - 1;
            int mask = maskUnshifted << offset;
            int value = (flags & mask) >> offset;
            for (E e : values) {
                if (e.getNumber() == value) {
                    return e;
                }
            }
            return null;
        }

        @Override
        public int toFlags(E value) {
            return value.getNumber() << offset;
        }
    }
}
