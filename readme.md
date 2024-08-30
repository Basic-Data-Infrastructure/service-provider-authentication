# BDI Service Provider -- Authentication

Clojure middleware for implementing the machine-to-machine
authentication mechanisms of a BDI service provider.

Also provides an `Association` clojure protocol as the basic data
source for assocation information, with an implementation for
in-memory data, and an implementation that fetches the association
information from a remote Association Register.

This is a work in progress and might be split up at a later date. The
immediate goal for this project is to provide a shared basis for
implementing an Assocation Register and an Authorization Register.
