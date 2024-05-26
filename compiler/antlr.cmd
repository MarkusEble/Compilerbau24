REM set environment variable JAVA_HOME; run in directory of antlr.cmd
set classpath=.;..\antlr-4.13.1-complete.jar
%JAVA_HOME%\bin\java org.antlr.v4.Tool language.g4 -o antlrcompiler -package compiler.antlrcompiler -visitor -listener