name := "Grouper"

organizationName := "com.expr"

scalaVersion := "2.12.6"

val kettleVersion = "7.0.0.0-25"

resolvers ++= Seq(
  "pentaho-releases" at "https://nexus.pentaho.org/content/groups/omni/",
  "swt-repo" at "https://swt-repo.googlecode.com/svn/repo/",
  "commons" at "https://mvnrepository.com/artifact/org.apache.commons/commons-vfs2"
)

libraryDependencies ++= Seq(
  "org.apache.commons"   % "commons-lang3"                      % "3.3.2"       % "provided",
  "org.apache.commons" % "commons-vfs2" % "2.0",
  "commons-httpclient"   % "commons-httpclient"                 % "3.1"         % "provided",
  "commons-vfs"          % "commons-vfs"                        % "1.0"         % "provided",
  "pentaho-kettle"       % "kettle-engine"                      % kettleVersion % "provided",
  "pentaho-kettle"       % "kettle-core"                        % kettleVersion % "provided",
  "pentaho-kettle"       % "kettle-ui-swt"                      % kettleVersion % "provided",
  "org.eclipse.swt"      % "org.eclipse.swt.win32.win32.x86_64" % "4.3"         % "provided"
)
