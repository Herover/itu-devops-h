# Minitwit documentation

Uses asciidoc as format.

Tools required: [asciidoctor](https://docs.asciidoctor.org/asciidoctor) and [Asciidoctor PDF](https://docs.asciidoctor.org/pdf-converter).

For local builds, use the docker file

```
docker build --tag minitwit-docs:latest .
```

```
docker run --rm --volume "`pwd`:/data" minitwit-docs -o docs.pdf index.adoc
```
