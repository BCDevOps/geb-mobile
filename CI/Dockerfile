FROM openshift/jenkins-slave-base-centos7
#FROM registry.access.redhat.com/openshift3/jenkins-slave-base-rhel7

EXPOSE 8080

ENV PATH=$HOME/.local/bin/:$PATH \
    LC_ALL=en_US.UTF-8 \
    LANG=en_US.UTF-8

ENV SUMMARY="Jenkins slave for mobile-BDDStack." \
    DESCRIPTION="Jenkins pipeline slave for running mobile test on cloud based devices."

LABEL summary="$SUMMARY" \
      description="$DESCRIPTION" \
      io.k8s.description="$DESCRIPTION" \
      io.k8s.display-name="Jenkins-Pipeline-mobile-BDDStack" \
      io.openshift.expose-services="8080:http" \
      io.openshift.tags="builder,jenkins-jnlp" \
      release="1"

# NOTES:
# We need to call 2 (!) yum commands before being able to enable repositories properly
# This is a workaround for https://bugzilla.redhat.com/show_bug.cgi?id=1479388
# Chrome install info: https://access.redhat.com/discussions/917293
RUN yum repolist > /dev/null && \
    yum install -y yum-utils && \
    yum-config-manager --disable \* &> /dev/null && \
    yum-config-manager --enable rhel-server-rhscl-7-rpms && \
    yum-config-manager --enable rhel-7-server-rpms && \
    yum-config-manager --enable rhel-7-server-optional-rpms && \
    yum-config-manager --enable rhel-7-server-fastrack-rpms && \
    UNINSTALL_PKGS="java-1.8.0-openjdk-headless.i686" &&\
    INSTALL_PKGS="java-1.8.0-openjdk" && \
    yum remove -y $UNINSTALL_PKGS &&\
    yum install -y --setopt=tsflags=nodocs $INSTALL_PKGS && \
    rpm -V $INSTALL_PKGS && \
    yum clean all -y && \
    if [ ! -d /home/jenkins/.pki ] ; then mkdir /home/jenkins/.pki; fi && \
    chmod 777 /home/jenkins/.pki

## install java openjdk:
ENV JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.161-0.b14.el7_4.x86_64/jre
ENV PATH=$JAVA_HOME/bin:$PATH
ENV OPENSHIFT_JENKINS_JVM_ARCH=x86_64

##------------------------- For cloud-based device connection ---------------
### No appium needs to be installed as apps are being tested on the cloud devices
### Import project file:
RUN mkdir /opt/app
ADD ../. /opt/app/

RUN /opt/app/./gradlew -i clean androidOnBrowserStack

USER 1001