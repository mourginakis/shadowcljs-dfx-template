import Principal "mo:base/Principal";

actor {

    var counter : Nat = 0;

    public func incrementCount() : async Nat {
        counter += 1;
        counter
    };

    public query func getCount() : async Nat {
        counter
    };

    public func resetCount() : async Nat {
        counter := 0;
        counter
    };

    //
    // misc & system funcs
    public shared ({caller}) func whoami() : async Principal {caller};

}