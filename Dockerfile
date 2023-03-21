FROM tomcat:9.0.71

ADD build/libs/ESE-Oye-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]