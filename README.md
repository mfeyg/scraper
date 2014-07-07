# scraper

A screenscraper that accepts JSON commands

## Usage

    $ lein run 5050
    $ curl -d '[["select", 
                ["get", "http://www.penny-arcade.com/comic/"],
                ["body", "img"],
                [0, "attrs", "src"]]]' \
              localhost:5050

## License

Copyright © 2014 Mendel Feygelson

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
