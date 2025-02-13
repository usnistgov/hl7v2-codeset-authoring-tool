./buildScript.sh
cp codeset-authoring-tool-backend/target/codeset-authoring-tool.jar .
docker build -t  codeset-authoring-tool-local .
