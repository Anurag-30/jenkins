FROM afbjorklund/centos-jnlp-slave
ADD kubectl /usr/local/bin/kubectl
ADD helm /usr/local/bin/helm
USER root
RUN yum install -y epel-release
RUN yum remove java-1.8.0-openjdk-headless.x86_64 -y
RUN yum install -y java-11-openjdk-headless.x86_64 java-11-openjdk.x86_64 java-11-openjdk-devel.x86_64
RUN yum install -y git wget && yum clean all
