FROM ruby:3.1.2-slim

#RUN apt-get update && apt-get install -y \
#    pandoc \
#    texlive-latex-base \
#    texlive-fonts-recommended \
#    texlive-extra-utils \
#    texlive-latex-extra

RUN apt-get update && apt-get install asciidoctor=2.0.12-2 -y
RUN gem install asciidoctor-pdf

WORKDIR "/data"

ENTRYPOINT [ "asciidoctor-pdf", "-r", "asciidoctor-pdf", "-b", "pdf" ]
