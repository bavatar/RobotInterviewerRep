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
