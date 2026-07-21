import {
  LayoutDashboard,
  TrendingUp,
  DollarSign,
  ClipboardList,
  AlertTriangle,
  FileText,
  Database,
  ChevronRight,
} from "lucide-react";

function Sidebar({ setQuestion }) {
  return (
    <aside className="sidebar">

      {/* Logo */}

      <div className="logo">

        <img
          src="https://cdn-icons-png.flaticon.com/512/3097/3097183.png"
          alt="Drone"
          className="logoImage"
        />

        <div>

          <h2>Skylark</h2>

          <span>Business Intelligence</span>

        </div>

      </div>

      {/* Dashboard */}

      <button className="menu active">

        <LayoutDashboard size={20} />

        <span>Dashboard</span>

      </button>

      {/* Quick Questions */}

      <h4>Quick Questions</h4>

      <button
        className="menu"
        onClick={() => setQuestion("How many open deals do we have?")}
      >
        <TrendingUp size={18} />
        <span>Open Deals</span>
        <ChevronRight size={16} />
      </button>

      <button
        className="menu"
        onClick={() => setQuestion("What is our total pipeline value?")}
      >
        <DollarSign size={18} />
        <span>Pipeline Value</span>
        <ChevronRight size={16} />
      </button>

      <button
        className="menu"
        onClick={() => setQuestion("How many completed work orders do we have?")}
      >
        <ClipboardList size={18} />
        <span>Work Orders</span>
        <ChevronRight size={16} />
      </button>

      <button
        className="menu"
        onClick={() => setQuestion("Show business risks")}
      >
        <AlertTriangle size={18} />
        <span>Business Risks</span>
        <ChevronRight size={16} />
      </button>

      <button
        className="menu"
        onClick={() => setQuestion("Give me an executive summary")}
      >
        <FileText size={18} />
        <span>Executive Summary</span>
        <ChevronRight size={16} />
      </button>

      {/* Bottom Card */}

      <div className="database">

        <div className="dbIcon">

          <Database size={22} />

        </div>

        <div>

          <strong>Connected</strong>

          <p>Monday.com Boards</p>

        </div>

      </div>

    </aside>
  );
}

export default Sidebar;