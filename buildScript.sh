set -e
cd frontend
npm install
npm run build
cd ..
cd codeset-authoring-tool-backend
mvn clean install -DskipTests