const CONFIG = {
    API_BASE_URL: 'http://localhost:8080/api',
    WS_RECONNECT_DELAY: 5000,
    MAX_RETRIES: 5
};

const state = {
    isRecording: false,
    isConnected: false,
    retryCount: 0
};

document.addEventListener('DOMContentLoaded', () => {
    const assistantSelect = document.getElementById('assistant-select');

    // Function to fetch assistants from the backend
    async function fetchAssistants() {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/dashboard/assistants`);
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const assistants = await response.json();
            populateDropdown(assistants);
        } catch (error) {
            console.error('Error fetching assistants:', error);
            assistantSelect.innerHTML = '<option value="">Error loading assistants</option>';
        }
    }

    function populateDropdown(assistants) {
        assistantSelect.innerHTML = '';
        
        const defaultOption = document.createElement('option');
        defaultOption.value = '';
        defaultOption.textContent = 'Select an assistant...';
        assistantSelect.appendChild(defaultOption);
        
        assistants.forEach(assistant => {
            const option = document.createElement('option');
            option.value = assistant.id;
            option.textContent = assistant.title;
            assistantSelect.appendChild(option);
        });
    }

    assistantSelect.addEventListener('change', (event) => {
        const selectedId = event.target.value;
        if (selectedId) {
            console.log('Selected assistant ID:', selectedId);
            //TODO do things when assistant is selected
        }
    });

    fetchAssistants();
});
