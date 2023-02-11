# Shadow-cljs + DFX
Template for getting Clojurescript/shadow-cljs running with DFX

A simple counter app

## The Stack
 - motoko + shadow-cljs + reagent/react + ~~hiccup~~ + ~~tailwindcss~~


## Getting Started
```bash
$ npx degit https://github.com/mourginakis/shadowcljs-dfx-template my_app_name
$ npm install
```
Shadow-cljs has two types of builds - a development build that uses
a large cljs runtime, and a production build that makes the compiled
js as small as possible.

We want to take advantage of the cljs runtime for fast reloads in our
development server using `npx shadow-cljs watch app`, but we obviously never
want to upload this large runtime to the canister itself.

The cljs runtime is located in `dist/counter_frontend/js/cljs-runtime/`.

DFX, when given a source folder for an asset canister, will upload the 
entire directory, without an option to ignore any subdirectories. The 
current solution for this is to delete everything in the source folder with `rm -r dist/counter_frontend/*`, 
and then recompile a production build with `npx shadow-cljs release app` 
before running `dfx deploy`.

Shadow-cljs, for development, also expects things like html files to be
inside the `dist/counter_frontend/` directory. In order to satisfy this, 
we need to copy any html files or assets into this directory. Check the 
:build-hooks in `shadow-cljs.edn` to see how this is done.


## The Development --> Production Pipeline
If you have any improvements to this flow please make a PR!

### 1. Run dev build on local replica
```bash
$ dfx start --background --clean

# Run only the backend canister on a local replica
$ dfx deploy counter_backend
$ dfx generate counter_backend

# Run the frontend from dist/ with the cljs runtime
$ npx shadow-cljs watch app
```
Access frontend with shadow-cljs server:  
<http://127.0.0.1:8080>


### 2. Test prod build on local replica (recommended)
```bash
# delete dev compilation and runtime
$ rm -r dist/counter_frontend/*

# recompile cljs into production ready js
$ npx shadow-cljs release app

# deploy frontend and backend
$ dfx deploy
```
Access frontend from local replica:  
<http://127.0.0.1:4943>


### 3. Deploy prod build to mainnet
```bash
$ rm -r dist/counter_frontend/*
$ npx shadow-cljs release app
$ dfx deploy --network ic
```
Access from the IC:  
[https://{frontend_canister_id}.ic0.app](https://ic0.app)


## Resources

- <https://shadow-cljs.github.io/docs/UsersGuide.html>
- <https://ericnormand.me/guide/clojurescript-tutorial>
- <https://ericnormand.me/guide/reagent>
