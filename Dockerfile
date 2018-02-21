FROM registry.access.redhat.com/openshift3/jenkins-slave-base-rhel7

EXPOSE 8080

FROM centos:7

USER root

## install java:
RUN yum -y install java-1.8.0-openjdk
RUN java -version

ENV JAVA_HOME "/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.161-0.b14.el7_4.x86_64/jre"
RUN echo $JAVA_HOME
ENV PATH=$JAVA_HOME/bin:$PATH

#------------------------- For cloud-based device connection ---------------
## No appium needs to be installed as apps are being tested on the cloud devices
## Import project file:
ADD . /geb-mobile




