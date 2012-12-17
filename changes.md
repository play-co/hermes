Changes 
* Dec 17th, 2012 - Bumped project number to 0.2.6-SNAPSHOT. Titan to 0.2-SNAPSHOT. No
  API changes. Watch out for a change in how Titan stores id's. 
* Dec 15th, 2012 - Bumped project number to 0.2.5. Added in support
  for storing common clojure data structures. Take a look at kyro.clj
  for more information as well as the corresponding tests.
* Dec 14th, 2012 - Bumped project number to 0.2.4. Changed v/get-by-id
  to v/find-by-id and added in a analog for edges. Added in delete!
  methods with tests. Changed prop-map to return with `:__id__` instead
  of `:id`, and added in `:__label` to edge. 
* Dec 3rd, 2012 - Bumped project number to 0.2.3. Whenever a key of an
  object is returned, it must be a keyword. Likewise, whenever a
  collection is returned by a function it must be a clojure
  collection, i.e. PersistentHashSet instead of HashSet. Tests have
  been updated to reflect this. 
* Dec 3rd, 2012 - Started change log with version 0.2.2. 
