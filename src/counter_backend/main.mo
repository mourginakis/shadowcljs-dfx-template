import Principal "mo:base/Principal";

actor {

    //
    // misc & system funcs
    public shared ({caller}) func whoami() : async Principal {caller};

}