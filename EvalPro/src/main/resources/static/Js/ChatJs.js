async function sendMessage() {
    let userInput = document.getElementById("userInput").value.trim();
    if (userInput === "") return;

    // Append user's message
    appendMessage("You", userInput, "bg-blue-100");

    // Show typing animation
    showTypingIndicator();

    // Call API
    try {
        let response = await fetch("http://localhost:8080/chat/ask", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userInput)
        });

        let botResponse = await response.text();

        // Hide typing animation
        hideTypingIndicator();

        // Append bot's message
        appendMessage("EduBot", botResponse, "bg-gray-200");
    } catch (error) {
        hideTypingIndicator();
        appendMessage("EduBot", "Oops! Something went wrong. Please try again.", "bg-red-200");
    }

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

// Function to show typing animation
function showTypingIndicator() {
    let chatbox = document.getElementById("chatbox");

    let typingDiv = document.createElement("div");
    typingDiv.id = "typing-indicator";
    typingDiv.className = "p-2 rounded-lg bg-gray-200 w-fit max-w-xs self-start flex items-center";
    typingDiv.innerHTML = `<strong>EduBot:</strong> <span class="typing-dots">•</span><span class="typing-dots">•</span><span class="typing-dots">•</span>`;

    chatbox.appendChild(typingDiv);
    chatbox.scrollTop = chatbox.scrollHeight; // Auto-scroll to latest message
}

// Function to hide typing animation
function hideTypingIndicator() {
    let typingDiv = document.getElementById("typing-indicator");
    if (typingDiv) {
        typingDiv.remove();
    }
}
