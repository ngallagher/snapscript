package org.snapscript.core.convert;

import java.util.HashMap;
import java.util.Map;

import org.snapscript.core.InstanceChecker;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeExtractor;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.TypeVerifier;

public class ConstraintMatcher {
   
   private final Map<Type, ConstraintConverter> converters;
   private final TypeExtractor extractor;
   private final TypeVerifier comparator;
   private final ConstraintConverter converter;
   private final InstanceChecker checker;
   
   public ConstraintMatcher(TypeLoader loader) {
      this.converters = new HashMap<Type, ConstraintConverter>();
      this.checker = new InstanceChecker();
      this.comparator = new TypeVerifier(loader, checker);
      this.extractor = new TypeExtractor(loader);
      this.converter = new NullConverter();
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
      if(comparator.same(Object.class, type)) {
         return new AnyConverter();
      }
      if(comparator.same(double.class, type)) {
         return new DoubleConverter(type);
      }
      if(comparator.same(float.class, type)) {
         return new FloatConverter(type);
      }
      if(comparator.same(int.class, type)) {
         return new IntegerConverter(type);
      }
      if(comparator.same(long.class, type)) {
         return new LongConverter(type);
      }
      if(comparator.same(short.class, type)) {
         return new ShortConverter(type);
      }
      if(comparator.same(byte.class, type)) {
         return new ByteConverter(type);
      }
      if(comparator.same(char.class, type)) {
         return new CharacterConverter(type);
      }
      if(comparator.same(boolean.class, type)) {
         return new BooleanConverter(type);
      }
      if(comparator.same(Number.class, type)) {
         return new NumberConverter(type);
      }
      if(comparator.same(Double.class, type)) {
         return new DoubleConverter(type);
      }
      if(comparator.same(Float.class, type)) {
         return new FloatConverter(type);
      }
      if(comparator.same(Integer.class, type)) {
         return new IntegerConverter(type);
      }
      if(comparator.same(Long.class, type)) {
         return new LongConverter(type);
      }
      if(comparator.same(Short.class, type)) {
         return new ShortConverter(type);
      }
      if(comparator.same(Byte.class, type)) {
         return new ByteConverter(type);
      }
      if(comparator.same(Character.class, type)) {
         return new CharacterConverter(type);
      }
      if(comparator.same(Boolean.class, type)) {
         return new BooleanConverter(type);
      }
      if(comparator.same(String.class, type)) {
         return new StringConverter();
      }
      if(comparator.like(Scope.class, type)) {
         return new ScopeConverter();
      }
      if(comparator.like(Enum.class, type)) {
         return new EnumConverter(type);
      }      
      return new ObjectConverter(extractor, checker, type);
   }
}
