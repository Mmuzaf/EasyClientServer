# EasyClientServer
Java terminal client server application and multi-client-server using TCP connections with sockets.

## Usage
Setup this application to work as server, or as a client.

#### Server
`java -jar  SimpleTCPClientServer-[CURRENT_VERSION]-SNAPSHOT.jar -l port`

example: `java -jar  SimpleTCPClientServer-1.0-SNAPSHOT.jar -l 8001`

#### Client

 `java -jar SimpleTCPClientServer-[CURRENT_VERSION]-SNAPSHOT.jar -l hostname 8001`

 example: `java -jar SimpleTCPClientServer-1.0-SNAPSHOT.jar -l localhost 8001`

##### Available commands for client application

/online          - Return number of connected clients

/expand [number] - Get number multipliers

/exit            - Disconnect normally from server

/help            - Get additional information


## Building sources
Intellij IDEA 15.0.1 was used and JDK 8 environment. However any java compiler should work if source files are setup correctly.


## Author
**Maksim Muzafarov**

## License
  >Copyright (C) 2016 Maksim Muzafarov
  >
  >This program is free software: you can redistribute it and/or modify
  >it under the terms of the GNU General Public License as published by
  >the Free Software Foundation, either version 3 of the License, or
  >(at your option) any later version.
  >
  >This program is distributed in the hope that it will be useful,
  >but WITHOUT ANY WARRANTY; without even the implied warranty of
  >MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  >GNU General Public License for more details.
  >
  >You should have received a copy of the GNU General Public License
  >along with this program.  If not, see <http://www.gnu.org/licenses/>.








