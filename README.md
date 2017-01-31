# compojure-experiments

Wrote an API for `/things` backed by redis for fun using clojure. 

## Impressions

So far I don't really like clojure. I like the mindset it puts me in, but simple things like modifying chains of function calls can be annoying. With compilation being pretty slow, time lost on parenthesis errors can really start to add up. That being said, I'm pretty sure that my unhappiness comes from not taking full advantage of language features like the thread-first macro:

```clojure
(-> x & forms)
```

It doesn't help either that there aren't many examples online and that the language has many features with names that don't map to other languages. Reading documentation for the thread-first macro made no sense to me:

	Threads the expr through the forms. Inserts x as the
	second item in the first form, making a list of it if it is not a
	list already. If there are more forms, inserts the first form as the
	second item in second form, etc.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server-headless
