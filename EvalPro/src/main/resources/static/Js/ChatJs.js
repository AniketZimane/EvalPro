async function sendMessage() {
    let userInput = document.getElementById("userInput").value.trim();
    if (userInput === "") return;

    // Append user's message
    appendMessage("You", userInput, "bg-blue-100");

    // Call API
    let response = await fetch("http://localhost:8080/chat/ask", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userInput)
    });

    let botResponse = await response.text();
    appendMessage("EduBot", botResponse, "bg-gray-200");

    // Clear input field
    document.getElementById("userInput").value = "";
}

// Function to append messages in chatbox
function appendMessage(sender, message, bgColor) {
    let chatbox = document.getElementById("chatbox");
    let messageDiv = document.createElement("div");
    messageDiv.className = `p-2 rounded-lg ${bgColor} w-fit max-w-xs ${sender === "You" ? "self-end ml-auto" : "self-start"}`;
    messageDiv.innerHTML = `<strong>${sender}:</strong> ${message}`;
    chatbox.appendChild(messageDiv);
    chatbox.scrollTop = chatbox.scrollHeight; // Auto-scroll to latest message
}
