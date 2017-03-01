TITLE "Selenium Node (Don't Close)"
java -Dwebdriver.chrome.driver="chromedriver.exe" -Dwebdriver.opera.driver="operadriver.exe"  -Dwebdriver.ie.driver="IEDriverServer_32.exe" -Dwebdriver.edge.driver="" -jar selenium-server-standalone.jar -role node -nodeConfig nodeconfig.json
rem java -Dwebdriver.chrome.driver="chromedriver.exe" -Dwebdriver.opera.driver="operadriver.exe"  -Dwebdriver.ie.driver="IEDriverServer_32.exe" -Dwebdriver.edge.driver="" -jar selenium-server-standalone.jar -role node 
taskkill /F /IM IEDriverServer.exe
taskkill /F /IM chromedriver.exe
pause