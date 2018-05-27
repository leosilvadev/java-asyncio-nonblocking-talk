# Async IO & Non-blocking IO inside JVM - Tech-Talk

All the code in the repo is used in my tech-talk about async io and non-blocking io inside JVM.

## What will going to find in the repo
- Simple TCP Server returning a list of Users as JSON implemented with Async IO (thread pool)
- Simple TCP Client that calls the TCP Server and print the result implemented with Async IO (thread pool)
- Simple TCP Server returning a list of Users as JSON implemented with non-blocking IO (channel, buffer and selector)
- Simple TCP Client that calls the TCP Server and print the result implemented with implemented with non-blocking IO (channel, buffer and selector)

**The non-blocking implementation has comments to better explain how does it works**