const paymentStrat = () => {
  console.log("Payment started");
  let amountEntered = 605;
  console.log(amountEntered);

  var endpoint = "/user/payment_order/";
  console.log(amountEntered);

  axios.post(endpoint, { amount: amountEntered })
    .then(function(response) {
        console.log(response.data)
        console.log(response.status)
      if (response.status === 200) {
        console.log("Execute")
        let options = {
          key: 'rzp_test_jD41V8O877iJax',
          amount: response.data.amount,
          currency: 'INR',
          name: 'Innovative Things',
          description: 'Donation',
          image: '/img/evalProLogo.png',
          order_id: response.data.id,
          handler: function(response) {
            console.log(response.razorpay_payment_id);
            console.log(response.razorpay_order_id);
            console.log(response.razorpay_signature);
            console.log("Payment successful");
            swal("Good job!", "Payment done successful!", "success");
          },
          prefill: {
            name:"", // Provide the customer's name
            email:"", // Provide the customer's email
            contact:"" // Provide the customer's phone number
          },
          notes: {
            address: "Insight Visioners"
          },
          theme: {
            color: "#3399cc"
          }
        };

        var rzp1 = new Razorpay(options);
        console.log("Razorpay object created:", rzp1);

        rzp1.on("payment.failed", function(response) {
            console.log(response.error.code)
            console.log(response.error.description)
            console.log(response.error.source)
            console.log(response.error.step)
            console.log(response.error.reason)
            console.log(response.error.metadata.order_id)
            console.log(response.error.metadata.payment_id)
        });

        rzp1.open();
        console.log("Razorpay window opened");
      }
    })
    .catch(function(error) {
      console.error(error);
    });
};

