@echo off
setlocal
echo ==========================================================
echo  MiniRAG Academico - Spring Boot + Spring AI + Groq RAG
echo ==========================================================

REM 1. Deteccion automatica del JDK 21
if "%JAVA_HOME%"=="" (
    if exist "%USERPROFILE%\.vscode\extensions\redhat.java-1.54.0-win32-x64\jre\21.0.10-win32-x86_64" (
        set "JAVA_HOME=%USERPROFILE%\.vscode\extensions\redhat.java-1.54.0-win32-x64\jre\21.0.10-win32-x86_64"
        set "PATH=%USERPROFILE%\.vscode\extensions\redhat.java-1.54.0-win32-x64\jre\21.0.10-win32-x86_64\bin;%PATH%"
    )
)

echo Java detectado en: %JAVA_HOME%

REM 2. Verificacion de GROQ_API_KEY
if "%GROQ_API_KEY%"=="" (
    echo.
    echo [!] No se encontro la variable de entorno GROQ_API_KEY.
    echo     Para obtenerla gratis: ve a https://console.groq.com -^> API Keys -^> Create API Key
    echo.
    set /p GROQ_API_KEY="Ingresa tu API Key de Groq (ej: gsk_...): "
    echo.
)

REM 3. Ejecutar aplicacion con Spring Boot
call .\mvnw.cmd spring-boot:run
pause
