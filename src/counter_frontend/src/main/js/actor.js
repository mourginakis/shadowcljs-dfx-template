import { Actor, HttpAgent } from "@dfinity/agent";

// Woah, ok, this took me some time to figure out.
// So besides the difficulty of importing javascript files that exist outside
// the classpath, and the fact that you can't use underscores in (:requre),
// which I didn't know, you straight up just can't even import 
// src/declarations/counter_backend/index.js because it expects 
// environment variables with `process.env.CANISTER_ID_ETC` to be set
// with webpack, and this just completely breaks functionality when trying 
// to import the javascript into clojurescript.

// So what we're going to have to do here is write our own custom actor
// (shouldn't be too hard to do) that takes an automatically generated IDL from
// our src/declarations/counter_backend/counter_backend.did.js file.
// We'll then import this clojurescript compatible actor into our app.cljs file
// using a standard cljs javascript interop import with (:require)



// Imports candid interface
// we can import directly from counter-backend-did because 
// we defined this as an external javascript file inside our 
// shadow-cljs.edn file with the :js-options tag.

import { idlFactory } from "counter-backend-did";

export const createActor = (isDevelopment, canisterId, options = {}) => {
  const agent = options.agent || new HttpAgent({ ...options.agentOptions });
  if (options.agent && options.agentOptions) {
    console.warn(
      "Detected both agent and agentOptions passed to createActor. Ignoring agentOptions and proceeding with the provided agent."
    );
  }

  if (isDevelopment) {
    agent.fetchRootKey().catch((err) => {
      console.warn(
        "Unable to fetch root key. Check to ensure that your local replica is running"
      );
      console.error(err);
    });
  }

  // Creates an actor with using the candid interface and the HttpAgent
  return Actor.createActor(idlFactory, {
    agent,
    canisterId,
    ...options.actorOptions,
  });
};
