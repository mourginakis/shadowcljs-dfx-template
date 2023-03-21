# Shadow-cljs + DFX
Template for getting Clojurescript/shadow-cljs running with DFX

A simple counter app  

## The Stack
 - motoko + shadow-cljs + reagent/react + tailwind
 
 For the no-tailwind version, check out the branch 'no-tailwind'


## Getting Started
```bash
$ npx degit https://github.com/mourginakis/shadowcljs-dfx-template my_app_name
$ npm install
```

## The Development --> Production Pipeline

### 1. Run dev build on local replica
```bash
$ dfx start --background --clean

# Run only the backend canister on a local replica
$ dfx deploy counter_backend
$ dfx generate counter_backend

# Run the frontend from dist/ with the cljs runtime
# or you can start by jacking in from your repl
$ npx shadow-cljs watch app

```
Access frontend with shadow-cljs server:  
<http://127.0.0.1:8080>


### 2. Test prod build on local replica (recommended)
Deploy frontend and backend to local replica
```bash
# 'dfx deploy' automatically runs 'npm run build', which
# -- deletes dev compilation and runtime
# -- recompiles cljs into production ready js into dist/
$ dfx deploy
```
Access frontend from local replica:  
<http://127.0.0.1:4943>


### 3. Deploy prod build to mainnet
Deploy frontend and backend to mainnet canister
```bash
# automatically runs 'npm run build'
$ dfx deploy --network ic
```
Access from the IC:  
[https://{frontend_canister_id}.ic0.app](https://ic0.app)


## Live Demo
This canister is hosted at:
<https://ehrj7-wyaaa-aaaag-qb2ya-cai.ic0.app/>

## Notes

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
before running `dfx deploy`. This is automatically done with the script `npm run build`

Shadow-cljs, for development, also expects things like html files to be
inside the `dist/counter_frontend/` directory. In order to satisfy this, 
we need to copy any html files or assets into this directory. Check the 
:build-hooks in `shadow-cljs.edn` to see how this is done.

`dfx deploy` always automatically calls `npm run build`

`dfx deploy`, when run, will automatically set the environment variable `DFX_NETWORK` to `local` or `ic`, depending upon the option you specified


## Resources

- <https://shadow-cljs.github.io/docs/UsersGuide.html>
- <https://ericnormand.me/guide/clojurescript-tutorial>
- <https://reagent-project.github.io/>
- <https://ericnormand.me/guide/reagent>
