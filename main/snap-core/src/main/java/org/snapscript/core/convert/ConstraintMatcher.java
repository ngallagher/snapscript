package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeCastChecker;
import org.snapscript.core.TypeExtractor;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.TypeVerifier;

public class ConstraintMatcher {
   
   private final Map<Type, ConstraintConverter> converters;
   private final ConstraintConverter converter;
   private final TypeExtractor extractor;
   private final TypeVerifier comparator;
   private final ProxyWrapper wrapper;
   private final TypeCastChecker checker;
   
   public ConstraintMatcher(TypeLoader loader, ProxyWrapper wrapper) {
      this.converters = new ConcurrentHashMap<Type, ConstraintConverter>();
      this.extractor = new TypeExtractor(loader);
      this.checker = new TypeCastChecker(this, extractor, loader);
      this.comparator = new TypeVerifier(loader, checker);
      this.converter = new NullConverter();
      this.wrapper = wrapper;
   }
   
   public ConstraintConverter match(Type type) throws Exception { // type declared in signature
      if(type != null) {
         ConstraintConverter converter = converters.get(type);
         
         if(converter == null) {
            converter = resolve(type);
            converters.put(type, converter);
         }
         return converter;
      }
      return converter;
   }
   
   private ConstraintConverter resolve(Type type) throws Exception {
      if(comparator.isSame(Object.class, type)) {
         return new AnyConverter(wrapper);
      }
      if(comparator.isSame(double.class, type)) {
         return new DoubleConverter(type);
      }
      if(comparator.isSame(float.class, type)) {
         return new FloatConverter(type);
      }
      if(comparator.isSame(int.class, type)) {
         return new IntegerConverter(type);
      }
      if(comparator.isSame(long.class, type)) {
         return new LongConverter(type);
      }
      if(comparator.isSame(short.class, type)) {
         return new ShortConverter(type);
      }
      if(comparator.isSame(byte.class, type)) {
         return new ByteConverter(type);
      }
      if(comparator.isSame(char.class, type)) {
         return new CharacterConverter(type);
      }
      if(comparator.isSame(boolean.class, type)) {
         return new BooleanConverter(type);
      }
      if(comparator.isSame(Number.class, type)) {
         return new NumberConverter(type);
      }
      if(comparator.isSame(Double.class, type)) {
         return new DoubleConverter(type);
      }
      if(comparator.isSame(Float.class, type)) {
         return new FloatConverter(type);
      }
      if(comparator.isSame(Integer.class, type)) {
         return new IntegerConverter(type);
      }
      if(comparator.isSame(Long.class, type)) {
         return new LongConverter(type);
      }
      if(comparator.isSame(Short.class, type)) {
         return new ShortConverter(type);
      }
      if(comparator.isSame(Byte.class, type)) {
         return new ByteConverter(type);
      }
      if(comparator.isSame(Character.class, type)) {
         return new CharacterConverter(type);
      }
      if(comparator.isSame(Boolean.class, type)) {
         return new BooleanConverter(type);
      }
      if(comparator.isSame(BigDecimal.class, type)) {
         return new BigDecimalConverter(type);
      }
      if(comparator.isSame(BigInteger.class, type)) {
         return new BigIntegerConverter(type);
      }
      if(comparator.isSame(AtomicLong.class, type)) {
         return new AtomicLongConverter(type);
      }
      if(comparator.isSame(AtomicInteger.class, type)) {
         return new AtomicIntegerConverter(type);
      }
      if(comparator.isSame(String.class, type)) {
         return new StringConverter();
      }
      if(comparator.isLike(Scope.class, type)) {
         return new ScopeConverter();
      }
      if(comparator.isLike(Enum.class, type)) {
         return new EnumConverter(type);
      }      
      return new ObjectConverter(extractor, checker, wrapper, type);
   }
}
