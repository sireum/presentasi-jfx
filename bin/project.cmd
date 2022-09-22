::#! 2> /dev/null                                   #
@ 2>/dev/null # 2>nul & echo off & goto BOF         #
if [ -z ${SIREUM_HOME} ]; then                      #
  echo "Please set SIREUM_HOME env var"             #
  exit -1                                           #
fi                                                  #
exec ${SIREUM_HOME}/bin/sireum slang run "$0" "$@"  #
:BOF
setlocal
if not defined SIREUM_HOME (
  echo Please set SIREUM_HOME env var
  exit /B -1
)
%SIREUM_HOME%\bin\sireum.bat slang run "%0" %*
exit /B %errorlevel%
::!#
// #Sireum

import org.sireum._
import org.sireum.project.{Module, Project, ProjectUtil, PublishInfo, Target}

val home = Os.slashDir.up.canon

val presentasiJfx = Module(
  id = "presentasi-jfx",
  basePath = (home / "jvm").string,
  subPathOpt = None(),
  deps = ISZ(),
  targets = ISZ(Target.Jvm),
  ivyDeps = ISZ(),
  sources = ISZ((Os.path("src") / "main" / "java").string),
  resources = ISZ(),
  testSources = ISZ(),
  testResources = ISZ(),
  publishInfoOpt = Some(PublishInfo(
    description = "JavaFX Utilities for Presentasi",
    url = "github.com/sireum/presentasi-jfx",
    licenses = ProjectUtil.bsd2,
    developers = ISZ(ProjectUtil.robby)
  ))
)

val prj = Project.empty + presentasiJfx

println(project.JSON.fromProject(prj, T))
