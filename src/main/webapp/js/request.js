export class Request{
    constructor(method, action) {
        this.form = document.createElement("form");
        this.form.method = method;
        if(this.form.method === "get"){
            this.getActionAnalyser(action);
        }else{
            this.form.action = action;
        }
        this.form.style.display = "none";
        document.body.appendChild(this.form);
    }
    submit() {
        this.form.submit();
    }

    getActionAnalyser(action){
        let params = action.split("?")[1].split("&");
        for (let i = 0; i < params.length; i++) {
            let param = params[i].split("=");
            let input = document.createElement("input");
            input.setAttribute("type", "text");
            input.setAttribute("name", param[0]);
            input.setAttribute("value", param[1]);
            this.form.appendChild(input);
        }
    }

    static sendPost(action){
        new this("post", action).submit();
    }
    static sendGet(action){
         new this("get", action).submit();
    }
}

window.Request = Request;
export class Request{
    constructor(method, action) {
        this.form = document.createElement("form");
        this.form.method = method;
        if(this.form.method === "get"){
            this.getActionAnalyser(action);
        }else{
            this.form.action = action;
        }
        this.form.style.display = "none";
        document.body.appendChild(this.form);
    }

    /**
     * Send the request
     */
    submit() {
        this.form.submit();
    }

    /**
     * PRIVATE (but JS doesn't want to)!
     *
     * Analyse the action and create the input for the get request
     * @param action
     */
    getActionAnalyser(action){
        let params = action.split("?")[1].split("&");
        for (let i = 0; i < params.length; i++) {
            let param = params[i].split("=");
            let input = document.createElement("input");
            input.setAttribute("type", "text");
            input.setAttribute("name", param[0]);
            input.setAttribute("value", param[1]);
            this.form.appendChild(input);
        }
    }

    /**
     * Create a post request with the given action
     * @param action the action to send (written as : "controller?param=value&param2=value2&...&paramN=valueN&")
     */
    static sendPost(action){
        new this("post", action).submit();
    }
    /**
     * Create a Get request with the given action
     * @param action the action to send (written as : "controller?param=value&param2=value2&...&paramN=valueN&")
     */
    static sendGet(action){
         new this("get", action).submit();
    }
}

// Export the class to be used in a JSP file
window.Request = Request;
export class Request{
    constructor(method, action) {
        this.form = document.createElement("form");
        this.form.method = method;
        if(this.form.method === "get"){
            this.getActionAnalyser(action);
        }else{
            this.form.action = action;
        }
        this.form.style.display = "none";
        document.body.appendChild(this.form);
    }

    /**
     * Send the request
     */
    submit() {
        this.form.submit();
    }

    /**
     * PRIVATE (but JS doesn't want to)!
     *
     * Analyse the action and create the input for the get request
     * @param action
     */
    getActionAnalyser(action){
        let params = action.split("?")[1].split("&");
        for (let i = 0; i < params.length; i++) {
            let param = params[i].split("=");
            let input = document.createElement("input");
            input.setAttribute("type", "text");
            input.setAttribute("name", param[0]);
            input.setAttribute("value", param[1]);
            this.form.appendChild(input);
        }
    }

    /**
     * Create a post request with the given action
     * @param action the action to send (written as : "controller?param=value&param2=value2&...&paramN=valueN&")
     */
    static sendPost(action){
        new this("post", action).submit();
    }
    /**
     * Create a Get request with the given action
     * @param action the action to send (written as : "controller?param=value&param2=value2&...&paramN=valueN&")
     */
    static sendGet(action){
         new this("get", action).submit();
    }
}

// Export the class to be used in a JSP file
window.Request = Request;