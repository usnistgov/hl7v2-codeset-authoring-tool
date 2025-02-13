set -e
cd frontend
cat .npmrc
npm install
npm run build
cd ..
cd codeset-authoring-tool-backend
mvn clean install -DskipTests