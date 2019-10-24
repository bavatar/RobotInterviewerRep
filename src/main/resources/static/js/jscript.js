// function to interview date is within 2 weeks
function validateDate() {
   var startDate = new Date();
   startDate.setDate(startDate.getDate() +1);
   if (startDate > (startDate.getDate() + 14)){
       alert("This date is more than two weeks away. Please choose another date.");
   }
}

// function to check interview time is within 30 minutes
function checkTime(){
    var timer = 30 * 60 * 1000;
    var selectDate = new Date().getTime()+ timer;
    var currentDate = new Date();
    if (currentDate > selectDate){
        alert("This interview is now voided and the job opportunity has been rescinded.")
    }
}

// function pop up to schedule time
function schedInterview(){
    document.getElementById('sched'.style.display = "block");
}

// datetimepicker function
function dateSched () {
    ('#datetimepicker1').datetimepicker({format: "dd-MM-yyyy HH:mm", minuteStep: 30});
}

// function schedule confirmation
function confirmsched(){
    alert("Your interview has been scheduled.")
}

// function for popup appeal form
function popAppeal(){
    document.getElementById('appeal'.style.display ="block");
}
// function email confirmation
function confirmation(){
    alert("Your appeal has been sent.")
}

