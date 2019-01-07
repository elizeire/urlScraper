#UrlScraper

 mvn package liberty:run-server (first run only)
 
 mvn liberty:run-server (to run)
 
 hot redeploy: mvn build (only)
 
 mvn liberty:stop-server (also useful)

URL to access the Application:
JSON response:
http://localhost:8080/urlScraper/from=https:%2F%2Fen.wikipedia.org%2Fwiki%2FEurope

CSV file:
http://localhost:8080/urlScraper/from=https:%2F%2Fen.wikipedia.org%2Fwiki%2FEurope/csv