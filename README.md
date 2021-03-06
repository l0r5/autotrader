# Auto-Trader App

Algorithmic Trading Bot Application, written in Java and SpringBoot.

Until now, the App supports trading via Kraken. The bot chooses from a set of strategies and trades
accordingly, fully automatic.

![Maven CI Master](https://github.com/l0r5/autotrader/workflows/Maven%20CI%20Master/badge.svg?branch=master)

## Requirements

- JDK 1.8
- Maven

## Getting Started

1. Check out the project.
2. Create an API Key with the required permissions in your Kraken Account. Just
   follow [this](https://support.kraken.com/hc/en-us/articles/360035317352-Generating-an-API-key-and-QR-code-for-the-Kraken-Pro-mobile-app)
   simple setup guide, in case you need help.
3. Create two files for each key and place them inside the project:

        .
        ├── ...
        ├── api                     
        │   ├── src    
        │       ├── main          
        │           ├── resources           
        │               ├── secrets                  
        │                   ├── private-key-api.txt       # Create
        │                   └── public-key-api.txt        # Create
        └── ...
   :heavy_exclamation_mark: Please be careful, the names need to match the description. If they do
   not, the App will not be able to read the secrets.

4. Maven build:
   ```console
    $ mvn clean install
    ```

5. Run BrokerApplication.java