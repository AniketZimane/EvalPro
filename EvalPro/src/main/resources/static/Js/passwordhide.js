function togglePasswordVisibility(fieldId, buttonId) {
      const passwordField = document.getElementById(fieldId);
      const button = document.getElementById(buttonId);
      if (passwordField.type === "password") {
        passwordField.type = "text";
        button.innerHTML = "👁️";
      } else {
        passwordField.type = "password";
        button.innerHTML = "👁️‍🗨️";
      }
    }

    function clearForm() {
      document.getElementById("registrationForm").reset();
    }