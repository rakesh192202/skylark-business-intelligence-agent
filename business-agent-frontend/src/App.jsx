import { useState } from "react";
import axios from "axios";
import ReactMarkdown from "react-markdown";
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
        "https://skylark-business-intelligence-agent.onrender.com/api/chat",
        {
          question
        }
      );

      setAnswer(response.data.answer);

    } catch (e) {

      setAnswer("❌ Error: " + e.message);

    } finally {

      setLoading(false);

    }
  }

  return (

    <div className="container">

      <h1>🚁 Skylark Business Intelligence Agent</h1>

      <p className="subtitle">
        Ask founder-level business questions
      </p>

      <textarea
        rows="4"
        value={question}
        placeholder="Example: How many open deals do we have?"
        onChange={(e) => setQuestion(e.target.value)}
      />

      <button onClick={askAI} disabled={loading}>
        {loading ? "Analyzing..." : "Ask AI"}
      </button>

      {loading &&

        <div className="loading">

          <div className="spinner"></div>

          <p>Analyzing business data...</p>

        </div>

      }

      {answer && (

        <div className="response">

          <ReactMarkdown>

            {answer}

          </ReactMarkdown>

        </div>

      )}

    </div>

  );

}

export default App;