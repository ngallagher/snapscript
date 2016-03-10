package org.snapscript.parse;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;

public class ReferenceGrammar implements Grammar {

   private final Cache<Integer, Matcher> matchers;
   private final MatcherResolver resolver; 
   
   public ReferenceGrammar(GrammarResolver resolver, String name, int index) {
      this(resolver, name, index, 100);
   }
   
   public ReferenceGrammar(GrammarResolver resolver, String name, int index, int capacity) {
      this.matchers = new LeastRecentlyUsedCache<Integer, Matcher>(capacity);
      this.resolver = new MatcherResolver(resolver, name, index);
   }   
   
   @Override
   public Matcher compile(int serial) {
      Matcher matcher = matchers.fetch(serial); 
      
      if(matcher == null) {
         matcher = resolver.resolve(serial);
         matchers.cache(serial, matcher);
      }
      return matcher;
   }  
}
