export default function Home() {
  return (
    <div>
      <div>
      </div>
        <form onSubmit={requestVideoInfo}>
          <input type={"url"} placeholder={"Youtube / Spotify URL"} required={true}/>
          <button type={"submit"}></button>
        </form>
      <div>

      </div>
    </div>
  )
}

function requestVideoInfo() {

}
