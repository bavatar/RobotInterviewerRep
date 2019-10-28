// functions to check if interviews are pending or expired

// check interview time is within 30 minutes
function checkTime(){
    var timer = 31 * 60 * 1000;
    var startDate = new Date().getTime()+ timer;
    var currentDate = new Date();
    if (currentDate > startDate){
        alert("This interview is now voided and the job opportunity has been rescinded.  You may choose to appeal this decision.")
    }else
    {
        alert("There are interviews still pending.  Please go to your home page to complete them.")
    }
}

// these functions are for the interview scheduler

// show pop up to schedule time
function schedInterview(){
    document.getElementById('sched'.style.display = "block");
}
// choose date and time
function dateSched () {
    ('#datetimepicker1').datetimepicker({format: "dd-MM-yyyy HH:mm", minuteStep: 30});
}
// check if interview date is within 2 weeks
function validateDate() {
    var startDate = new Date();
    startDate.setDate(startDate.getDate() +1);
    if (startDate > (startDate.getDate() + 14)){
        alert("This date is more than two weeks away. Please choose another date.");
    }
}
// submit and close form
function closeSched(f) {
    f.submit();
    window.close();
}
// schedule confirmation
function confirmsched(){
    alert("Your interview has been scheduled.")
}

// these functions are used to show appeal form and confirmation
// show popup
function popAppeal(){
    document.getElementById('appeal'.style.display ="block");
}

// submit and close form
function closeAppeal(f) {
    f.submit();
    window.close();
}

// email confirmation
function confirmation(){
    alert("Your appeal has been sent.")
}

