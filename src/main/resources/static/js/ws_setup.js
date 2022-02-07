function setup_ws(msg) {

        const c_msg = JSON.parse(msg.data);
        switch (c_msg.type) {
            case 'online':
             console.log("online");
                break;
            case 'follower':
            console.log("follower");
                break;
            case 'profile-update':
                console.log("profileUpdate");
                break;
            case 'lesson-state-update':
               console.log("lessonupdate")
                break;
            case 'text-stimulus':
            case 'question-stimulus':
            case 'url-stimulus':
               console.log("stimulus");
                break;
            case 'Graph':
              console.log("Graph");
                break;
            case 'StartedSolving':
                console.log("solving the problem..");
                break;
            case 'SolutionFound':
                console.log("hurray!! we have found a solution..");
                break;
            case 'InconsistentProblem':
                console.log("unsolvable problem..");
                break;
            case 'FlawCreated':
             console.log("FlawCreated");
                break;
            case 'FlawStateChanged':
               console.log("FlawStateChanged");
                break;
            case 'FlawCostChanged':
               console.log("FlawCostChanged")
                break;
            case 'FlawPositionChanged':
              console.log("FlawCostChanged2")
                break;
            case 'CurrentFlaw':
            console.log("FlawCostChanged3")
                break;
            case 'ResolverCreated':
                console.log("FlawCostChanged4")
                break;
            case 'ResolverStateChanged':
              console.log("FlawCostChanged5")
                break;
            case 'CurrentResolver':
              console.log("FlawCostChanged6")
                break;
            case 'CausalLinkAdded':
               console.log("FlawCostChanged7");
                break;
            case 'Timelines':
             console.log("FlawCostChanged8")
                break;
            case 'Tick':
               console.log("FlawCostChanged9")
                break;
            case 'StartingAtoms':
                console.log("StartingAtoms")
                break;
            case 'EndingAtoms':
               console.log("EndingAtoms")
                break;
            default:
                console.log(msg);
                break;
        }
}