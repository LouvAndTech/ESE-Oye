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