FROM ubuntu:20.04
USER root
RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive \
    apt-get -y install default-jre-headless && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
COPY init.sh ./produce.sh
COPY ./build/libs/app-all.jar ./
RUN chmod +x ./produce.sh
ENTRYPOINT ["/bin/bash","-c"]
CMD ["./produce.sh"]