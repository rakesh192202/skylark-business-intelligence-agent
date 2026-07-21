import { useState } from "react";
import axios from "axios";
import "./App.css";

function App() {

  const [question, setQuestion] = useState("");
  const [answer, setAnswer] = useState("");
  const [loading, setLoading] = useState(false);

  async function askAI() {

    if (!question.trim()) return;

    setLoading(true);

    try {

      const response = await axios.post(
        "http://localhost:8080/api/chat",
        {
          question: question
        }
      );

      setAnswer(response.data.answer);

    } catch (e) {

      setAnswer("Error : " + e.message);

    }

    setLoading(false);
  }

  return (

    <div className="container">

      <h1>🚁 Skylark Business Intelligence Agent</h1>

      <p>
        Ask founder-level business questions
      </p>

      <textarea

        rows="4"

        value={question}

        placeholder="How is our Energy sector pipeline?"

        onChange={(e)=>setQuestion(e.target.value)}

      />

      <button onClick={askAI}>

        Ask AI

      </button>

      {loading && <h3>Analyzing business data...</h3>}

      <div className="response">

        <pre>

          {answer}

        </pre>

      </div>

    </div>

  );

}

export default App;