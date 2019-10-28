FROM wbaa/rokku-dev-apache-atlas:latest

ENV ATLAS_HOME=/opt/atlas/

COPY apache-atlas-hive-hook-3.0.0-SNAPSHOT.tar ${ATLAS_HOME}/hook/hive/
RUN cd ${ATLAS_HOME}/hook/hive/ && tar xvf apache-atlas-hive-hook-3.0.0-SNAPSHOT.tar && \
    mv  apache-atlas-hive-hook-3.0.0-SNAPSHOT/* ./ && \
	rm -rf apache-atlas-hive-hook-3.0.0-SNAPSHOT.tar apache-atlas-hive-hook-3.0.0-SNAPSHOT

