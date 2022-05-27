FROM ruby:alpine3.15

#RUN apt-get update && apt-get install -y \
#    pandoc \
#    texlive-latex-base \
#    texlive-fonts-recommended \
#    texlive-extra-utils \
#    texlive-latex-extra

RUN apk add --no-cache asciidoctor=2.0.16-r1
RUN gem install asciidoctor-pdf

WORKDIR "/data"

ENTRYPOINT [ "asciidoctor-pdf", "-r", "asciidoctor-pdf", "-b", "pdf" ]
